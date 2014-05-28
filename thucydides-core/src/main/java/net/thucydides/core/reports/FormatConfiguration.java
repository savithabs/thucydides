package net.thucydides.core.reports;

import ch.lambdaj.Lambda;
import com.beust.jcommander.internal.Lists;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.inject.Inject;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class FormatConfiguration {


    private final static Logger LOGGER = LoggerFactory.getLogger(FormatConfiguration.class);

    public static String DEFAULT_FORMATS = "json,xml,html";

    private final List<OutcomeFormat> formats;

    @Inject
    public FormatConfiguration(EnvironmentVariables environmentVariables) {
        List<String> formatNames = Splitter.on(",").trimResults()
                                                   .splitToList(ThucydidesSystemProperty.OUTPUT_FORMATS
                                                           .from(environmentVariables, DEFAULT_FORMATS));

        formats = Lists.newArrayList();
        for(String format : formatNames) {
            formats.addAll(outcomeFormatFrom(format).asSet());
        }
    }

    public FormatConfiguration(OutcomeFormat... formatValues) {
        formats = Arrays.asList(formatValues);
    }

    private static Optional<OutcomeFormat> outcomeFormatFrom(String value) {
        try {
            return Optional.of(OutcomeFormat.valueOf(value.toUpperCase()));
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Illegal outcome format ignored: " + value);
            return Optional.absent();
        }
    }

    public List<OutcomeFormat> getFormats() {
        if (formats.isEmpty()) {
            throw new IllegalArgumentException("No valid output format has been defined.");
        }
        return formats;
    }

    public OutcomeFormat getPreferredFormat() {
        return getFormats().get(0);
    }
}
