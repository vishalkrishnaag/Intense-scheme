package org.intense;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Memory  {
        private final Map<String, Object> store = new ConcurrentHashMap<>();

        // Store value
        public void define(String name, Object value) {
            store.put(name, value);
        }

        // Retrieve value
        public Optional<Object> lookup(String name) {
            return Optional.ofNullable(store.get(name));
        }

        // Delete a variable
        public void delete(String name) {
            store.remove(name);
        }

        // Clear all variables (reset)
        public void clear() {
            store.clear();
        }
    }

