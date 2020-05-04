package com.example.customerservice.service;

import com.example.customerservice.model.dto.CustomerTagDto;
import com.example.customerservice.model.dto.CustomerTagWrapperDto;
import com.example.customerservice.model.dto.Time;
import com.example.customerservice.model.entity.Customer;
import com.example.customerservice.model.entity.CustomerType;
import com.example.customerservice.model.entity.Tag;
import com.example.customerservice.repository.CustomerRepository;
import com.example.customerservice.repository.TagRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.apache.logging.log4j.LogManager.getLogger;

@Service
@Transactional
public class CustomerService {

    private static final Logger LOG = getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final TagRepository tagRepository;

    public CustomerService(final CustomerRepository customerRepository,
                           final TagRepository tagRepository) {
        this.customerRepository = customerRepository;
        this.tagRepository = tagRepository;
    }

    public Page<Customer> findByCustomerType(final CustomerType customerType,
                                             final String sortBy,
                                             final Sort.Direction direction,
                                             int page,
                                             int size){
        LOG.debug("Received findByCustomerType request with {}, {}", customerType, sortBy);
        final PageRequest pageRequest = PageRequest.of(page, size, direction, sortBy);
        if(customerType == null){
            return customerRepository.findAll(pageRequest);
        }
        return customerRepository.findByType(customerType, pageRequest);
    }

    public void initialize() {
        LOG.debug("Generating Random data set");

        List<Tag> tags = StreamSupport.stream(tagRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());

        final List<CustomerType> customerTypes = Arrays.asList(CustomerType.values());
        final List<String> names = Lists.newArrayList(
                "Kris", "Edwards",
                "Erin", "Dean",
                "Jody", "Wood",
                "Taylor", "Jones",
                "Kris", "Mason",
                "Ali", "Holmes",
                "Lynn", "Murphy",
                "Steff", "Day",
                "Eli", "Richards",
                "Haiden", "Hamilton");

        Random random = new Random();
        List<String> attributeKey = Lists.newArrayList("city", "state", "country", "address");
        Map<String, List<String>> attributes = Maps.newHashMap();
        attributes.put("city", Lists.newArrayList("Bangalore", "Munich", "Bratislava", "Amsterdam"));
        attributes.put("state", Lists.newArrayList("Bayern", "Berlin", "NRW", "BW"));
        attributes.put("country", Lists.newArrayList("India", "Netherlands", "Slovakia", "Germany"));
        attributes.put("address", Lists.newArrayList("address1", "address2", "address3", "address4"));

        List<Customer> customerList = Lists.newArrayList();
        IntStream.range(1, 21).forEach(index -> {
            final Tag tag1 = tags.get(random.nextInt(tags.size()));
            final Tag tag2 = tags.get(random.nextInt(tags.size()));

            final CustomerType type = customerTypes.get(random.nextInt(customerTypes.size()));
            final LocalDateTime createdDate = LocalDateTime.now().minus(10 + index, DAYS);
            List<Integer> intList = Lists.newArrayList(5, 6, 7, 8);
            LocalDateTime contactedDate = null;
            LocalDateTime onBoardedDate = null;
            if(type.equals(CustomerType.PROSPECT) || type.equals(CustomerType.ONBOARDED)) {
                contactedDate = createdDate.plus(intList.get(random.nextInt(intList.size())), DAYS);
            }
            if(type.equals(CustomerType.ONBOARDED)) {
                onBoardedDate = contactedDate.plus(intList.get(random.nextInt(intList.size())), DAYS);
            }

            String firstName = names.get(random.nextInt(names.size()));
            String lastName = names.get(random.nextInt(names.size()));

            final ImmutableMap<String, String> attributeMap = getRandomAttributeMap(random, attributes, attributeKey);

            final Customer customer = Customer.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(firstName+index+"@"+lastName+index+".com")
                    .type(type)
                    .createdOn(createdDate)
                    .contactedOn(contactedDate)
                    .onboardedOn(onBoardedDate)
                    .tags(Sets.newHashSet(tag1, tag2))
                    .attributes(attributeMap)
                    .build();
            customerList.add(customer);
        });

        customerRepository.saveAll(customerList);
        LOG.debug("Inserted {} customers with random data", customerList.size());
    }

    private ImmutableMap<String, String> getRandomAttributeMap(final Random random,
                                                               final Map<String, List<String>> attributes,
                                                               final List<String> attributeKey) {
        Collections.shuffle(attributeKey);

        final String attribute1 = attributeKey.get(0);
        final String attribute2 = attributeKey.get(1);

        final List<String> values1 = attributes.get(attribute1);
        final String value1 = values1.get(random.nextInt(values1.size()));

        final List<String> values2 = attributes.get(attribute2);
        final String value2 = values2.get(random.nextInt(values2.size()));

        return ImmutableMap.of(attribute1, value1, attribute2, value2);
    }

    public Time calculateAverageLeadTime(final CustomerType customerType) {
        AtomicLong totalCount = new AtomicLong();
        AtomicLong totalTime = new AtomicLong();

        customerRepository.findByType(customerType)
                .forEach(customer -> {
                    totalCount.getAndIncrement();
                    switch(customerType) {
                        case PROSPECT:
                            long prospectLeadTime = DAYS.between(customer.getCreatedOn(), customer.getContactedOn());
                            totalTime.getAndAdd(prospectLeadTime);
                            break;
                        case ONBOARDED:
                            long onboardLeadTime = DAYS.between(customer.getContactedOn(), customer.getOnboardedOn());
                            totalTime.getAndAdd(onboardLeadTime);
                    }
                });
        final long days = totalTime.get() / totalCount.get();
        LOG.info("Average lead time for {} customers is {} days", customerType, days);
        return Time.builder().days(days).unit(DAYS).build();
    }

    public CustomerTagWrapperDto groupCustomersByTag() {
        final Map<String, Set<Customer>> tagWithCustomerListMap = StreamSupport.stream(tagRepository.findAll().spliterator(), false)
                .collect(Collectors.toMap(Tag::getLabel, Tag::getCustomers));

        final List<CustomerTagDto> collect = tagWithCustomerListMap.entrySet()
                .stream()
                .map(entry -> CustomerTagDto.builder()
                        .tag(entry.getKey())
                        .customers(entry.getValue())
                        .totalCustomersCount(entry.getValue().size())
                        .build())
                .collect(Collectors.toList());
        return CustomerTagWrapperDto.builder().customerTagDtoList(collect).build();
    }

    public CustomerTagDto getCustomersByTag(String tag) {
        return tagRepository.findByLabel(tag)
                .map(tagFound -> CustomerTagDto.builder()
                        .tag(tagFound.getLabel())
                        .customers(tagFound.getCustomers())
                        .totalCustomersCount(tagFound.getCustomers().size())
                        .build())
                .orElse(CustomerTagDto.builder().build());

    }
}
