/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.pepzer.spring_boot_sample;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.LongSummaryStatistics;
import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockMeasurements implements Measurements {

    private static final Logger logger = LoggerFactory
        .getLogger(MockMeasurements.class);

    private final long seed;
    private List<Measurement> cache;

    public MockMeasurements() {
        this(0L);
    }

    public MockMeasurements(long seed) {
        this.seed = seed;
    }

    @Override
    public List<Measurement> getAll() {
        cache = genMockData();
        return cache;
    }

    private List<Measurement> getCache() {
        if (cache == null)
            return getAll();
        return cache;
    }

    public List<Measurement> genMockData() {

        Random random;
        if (seed == 0l)
            random = new Random();
        else
            random = new Random(seed);

        int measureLen = (int) (random.nextFloat() * 20 + 5);
        List<Measurement> mockData = new ArrayList<Measurement>(measureLen);

        String timestamp;
        long value;
        String status;
        String node;
        Measurement measure;

        for (int i = 0; i < measureLen; i++) {
            timestamp = LocalDateTime.parse("2018-11-24T00:00:00")
                .plusSeconds((long) random.nextInt() % 100000)
                .toString();

            value = (long) Math.abs(random.nextInt() % 1000);
            status = (value < 500) ? "Normal" : "Critical";
            node = String.format("Node%d",
                                 (int) (random.nextFloat() * 10));

            measure = new Measurement(timestamp, value, status, node);
            mockData.add(measure);
        }

        return mockData;
    }

    @Override
    public List<Measurement> getFiltered(String status, String node,
                                         String since, String until) {

        LocalDateTime tmpDate;
        try {
            tmpDate = LocalDateTime.parse(since);
        }
        catch (DateTimeParseException e) {
            logger.info("Invalid since format, will be ignored.");
            tmpDate = null;
        }
        final LocalDateTime sinceDate = tmpDate;

        try {
            tmpDate = LocalDateTime.parse(until);
        }
        catch (DateTimeParseException e) {
            logger.info("Invalid until format, will be ignored.");
            tmpDate = null;
        }
        final LocalDateTime untilDate = tmpDate;

        List<Measurement> res = getCache().stream()
            .filter(m -> (node.equals("") ||
                          m.getNode().equals(node)))
            .filter(m -> (status.equals("") ||
                          m.getStatus().equals(status)))
            .filter(m -> (sinceDate == null ||
                          LocalDateTime
                          .parse(m.getTimestamp())
                          .compareTo(sinceDate) >= 0))
            .filter(m -> (untilDate == null ||
                          LocalDateTime
                          .parse(m.getTimestamp())
                          .compareTo(untilDate) <= 0))
            .collect(Collectors.toList());

        return res;
    }

    @Override
    public List<AvgMeasure> getAvgByStatus() {
        Map<String, LongSummaryStatistics> stats = getCache().stream()
            .collect(Collectors
                     .groupingBy(Measurement::getStatus,
                                 Collectors
                                 .summarizingLong(Measurement::getValue)));

        List<AvgMeasure> res = new ArrayList<>();
        for (String k : stats.keySet()) {
                res.add(new AvgMeasure("status", k,
                                       stats.get(k).getAverage()));
        }
        return res;
    }

    @Override
    public List<AvgMeasure> getAvgByNode() {
        Map<String, LongSummaryStatistics> stats = getCache().stream()
            .collect(Collectors
                     .groupingBy(Measurement::getNode,
                                 Collectors
                                 .summarizingLong(Measurement::getValue)));

        List<AvgMeasure> res = new ArrayList<>();
        for (String k : stats.keySet()) {
            res.add(new AvgMeasure("node", k,
                                   stats.get(k).getAverage()));
        }
        return res;
    }
}
