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

import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeasurementsController {

    private static final Measurements measurements = new MockMeasurements();
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/measurements")
    public RestReply<Measurement>
        reqMeasurements(@RequestParam(value="status", defaultValue="")
                        String status,
                        @RequestParam(value="node", defaultValue="")
                        String node,
                        @RequestParam(value="since", defaultValue="")
                        String since,
                        @RequestParam(value="until", defaultValue="")
                        String until) {

        RestReply<Measurement> reply;

        if (status.equals("") && node.equals("") &&
            since.equals("") && until.equals("")) {
            reply = new RestReply<>(counter.incrementAndGet(),
                                    measurements.getAll());
        } else {
            reply = new RestReply<>(counter.incrementAndGet(),
                                    measurements.getFiltered(status, node,
                                                             since, until));
        }
        return reply;
    }

    @RequestMapping("/avg-by-status")
    public RestReply<AvgMeasure> reqAvgByStatus() {
        RestReply<AvgMeasure> reply;
        reply = new RestReply<>(counter.incrementAndGet(),
                                measurements.getAvgByStatus());
        return reply;
    }

    @RequestMapping("/avg-by-node")
    public RestReply<AvgMeasure> reqAvgByNode() {
        RestReply<AvgMeasure> reply;
        reply = new RestReply<>(counter.incrementAndGet(),
                                measurements.getAvgByNode());
        return reply;
    }
}
