/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.phuongtd.moolamoola.org.apache.tika.sax.xpath;

/**
 * Intermediate evaluation state of a <code>.../name...</code> XPath
 * expression. Matches nothing, but specifies the evaluation state
 * for the child elements with the given name.
 */
public class NamedElementMatcher extends ChildMatcher {

    private final String namespace;

    private final String name;

    protected NamedElementMatcher(String namespace, String name, Matcher then) {
        super(then);
        this.namespace = namespace;
        this.name = name;
    }

    public Matcher descend(String namespace, String name) {
        if (equals(namespace, this.namespace) && name.equals(this.name)) {
            return super.descend(namespace, name);
        } else {
            return FAIL;
        }
    }

    private static boolean equals(String a, String b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

}
