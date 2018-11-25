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

public interface Measurements {
    /** Return a list of all measurements */
    List<Measurement> getAll();

    /** Should be implemented to return a filtered list,
     *  some or all of the arguments could be null */
    List<Measurement> getFiltered(String status, String node,
                                  String since, String until);

    /** Should return a List of AvgMeasure computed grouping by Status */
    List<AvgMeasure> getAvgByStatus();

    /** Should return a List of AvgMeasure computed grouping by Node */
    List<AvgMeasure> getAvgByNode();
}
