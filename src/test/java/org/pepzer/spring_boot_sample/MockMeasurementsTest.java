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

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

public class MockMeasurementsTest {

    private final long seed = 123L;
    private final Measurements mock = new MockMeasurements(seed);

    @Test
    public void testGetAll() {
        List<Measurement> data = mock.getAll();
        assertEquals(19, data.size());
        assertEquals("Critical", data.get(0).getStatus());
        assertEquals("Node3", data.get(0).getNode());
        assertEquals(943, data.get(0).getValue());
        assertEquals("2018-11-24T15:15:01", data.get(0).getTimestamp());
    }

    @Test
    public void testGetFiltered() {
        List<Measurement> data = mock.getFiltered("invalid status", "", "", "");
        assertEquals(data.size(), 0);

        data = mock.getFiltered("Normal", "", "", "");
        assertEquals(9, data.size());
        assertEquals("Node6", data.get(0).getNode());
        assertEquals(125, data.get(0).getValue());
        assertEquals(108, data.get(8).getValue());

        data = mock.getFiltered("Normal", "Node6", "", "");
        assertEquals(3, data.size());
        assertEquals(125, data.get(0).getValue());
        assertEquals("2018-11-25T00:21:14", data.get(0).getTimestamp());
        assertEquals("2018-11-24T03:05:30", data.get(1).getTimestamp());
        assertEquals("2018-11-24T12:27:12", data.get(2).getTimestamp());

        data = mock.getFiltered("Normal", "Node6",
                                "2018-11-24T10:00:00",
                                "");
        assertEquals(2, data.size());
        assertEquals(452, data.get(1).getValue());

        data = mock.getFiltered("Normal", "Node6",
                                "2018-11-24T10:00:00",
                                "2018-11-24T20:00:00");
        assertEquals(1, data.size());
        assertEquals(452, data.get(0).getValue());
    }

    @Test
    public void testAvgByStatus() {
        List<Measurement> data = mock.getAll();

        long normalSum = 0;
        int normalCount = 0;
        long criticalSum = 0;
        int criticalCount = 0;
        for (Measurement m : data) {
            if (m.getStatus().equals("Normal")) {
                normalSum += m.getValue();
                ++normalCount;
            } else {
                criticalSum += m.getValue();
                ++criticalCount;
            }
        }

        double normalAvg = (double) normalSum / normalCount;
        double criticalAvg = (double) criticalSum / criticalCount;

        AvgMeasure normalAvgM;
        AvgMeasure criticalAvgM;
        List<AvgMeasure> avgMeasures = mock.getAvgByStatus();

        if (avgMeasures.get(0).getGroupId().equals("Normal")) {
            normalAvgM = avgMeasures.get(0);
            criticalAvgM = avgMeasures.get(1);
        } else {
            criticalAvgM = avgMeasures.get(0);
            normalAvgM = avgMeasures.get(1);
        }

        assertEquals(normalAvg, normalAvgM.getAvgValue(), 0.01);
        assertEquals(criticalAvg, criticalAvgM.getAvgValue(), 0.01);
    }

    @Test
    public void testAvgByNode() {
        List<Measurement> data = mock.getAll();

        long node0Sum = 0;
        int node0Count = 0;
        for (Measurement m : data) {
            if (m.getNode().equals("Node0")) {
                node0Sum += m.getValue();
                ++node0Count;
            }
        }
        assertEquals(3, node0Count);

        double node0Avg = (double) node0Sum / node0Count;

        AvgMeasure avgMeasureNode0 = null;
        List<AvgMeasure> avgMeasures = mock.getAvgByNode();
        for (AvgMeasure avgMeasure : avgMeasures) {
            if (avgMeasure.getGroupId().equals("Node0")) {
                avgMeasureNode0 = avgMeasure;
                break;
            }
        }

        assertNotNull(avgMeasureNode0);
        assertEquals(node0Avg, avgMeasureNode0.getAvgValue(), 0.01);
    }
}
