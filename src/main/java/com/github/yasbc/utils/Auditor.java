package com.github.yasbc.utils;


import java.util.ArrayList;
import java.util.List;

public final class Auditor {

    private static ThreadLocal<List<String>> entries = new ThreadLocal<List<String>>() {
        @Override
        protected List<String> initialValue() {
            return new ArrayList<>();
        }
    };

    private Auditor() {
    }

    public static void entry(final String entryName) {
        final List<String> entry = entries.get();
        entry.add("--- " + entryName);
    }

    public static void entry(final String entryName, final String message) {
        final List<String> entry = entries.get();
        entry.add("--- " + entryName);
        entry.add(message);
    }

    public static void message(final String message) {
        final List<String> entry = entries.get();
        entry.add(message);
    }

    public static void exception(final Exception e) {
        final List<String> entry = entries.get();
        entry.add(e.getMessage());
    }

    public static void exception(final String m) {
        final List<String> entry = entries.get();
        entry.add(m);
    }

    /**
     * Logs exception class, exception message, execution class and line number of the root cause.
     *
     * @param e the Exception
     */
    public static void debug(final Exception e) {
        final StringBuilder sb = new StringBuilder();
        final Throwable t = findRootCause(e);
        sb.append(t.toString());
        final StackTraceElement[] stackTrace = t.getStackTrace();
        if (stackTrace.length > 0) {
            final StackTraceElement stackTraceElement = stackTrace[0];
            sb.append(" at ").append(stackTraceElement.getClassName());
            sb.append(" : ").append(stackTraceElement.getLineNumber());
        }
        final List<String> entry = entries.get();
        entry.add(sb.toString());
    }

    public static Throwable findRootCause(final Throwable t) {
        Throwable rootCause = t;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    public static String flush() {
        return flush(false);
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public static String flush(final Boolean finalFlush) {
        final List<String> entry = entries.get();

        if (!entry.isEmpty()) {
            final StringBuilder result = new StringBuilder();
            for (final String s : entry) {
                result.append("    ").append(s).append('\n');
            }

            if (finalFlush) {
                entries.remove();
            } else {
                entries.set(new ArrayList<String>());
            }

            return result.toString();
        }

        return null;
    }
}

