/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.primepotato.jandas.io;


import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.utils.PrintType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.IntStream;

/**
 * A class that can pretty print a DataFrame to text for visualization in a console
 * <p>
 * Based off of https://github.com/zavtech/morpheus-core/blob/master/src/main/java/com/zavtech/morpheus/reference/XDataFramePrinter.java
 * under Apache 2 license
 */
public class DataFramePrinter {

    private final int maxRows;
    private final OutputStream stream;
    private PrintType printType;

    /**
     * Constructor
     *
     * @param maxRows the max rows to print
     * @param stream  the print stream to write to
     */
    public DataFramePrinter(int maxRows, OutputStream stream) {
        this(maxRows, PrintType.BOTH, stream);
    }


    public DataFramePrinter(int maxRows, PrintType pt, OutputStream stream) {
        this.printType = pt;
        this.maxRows = maxRows;
        this.stream = stream;
    }

    /**
     * Returns the column widths required to print the header and jandas
     *
     * @param headers the headers to print
     * @param data    the jandas items to print
     * @return the required column widths
     */
    private static int[] getWidths(String[] headers, String[][] data) {
        final int[] widths = new int[headers.length];
        maxWidth(widths, headers);
        for (String[] rowValues : data) {
            maxWidth(widths, rowValues);
        }
        return widths;
    }

    private static void maxWidth(int[] widths, String[] rowValues) {
        for (int j = 0; j < rowValues.length; j++) {
            final String value = rowValues[j];
            widths[j] = Math.max(widths[j], value != null ? value.length() : 0);
        }
    }

    /**
     * Returns the header template given the widths specified
     *
     * @param widths the token widths
     * @return the line format template
     */
    private static String getHeaderTemplate(int[] widths, String[] headers) {
        return IntStream.range(0, widths.length).mapToObj(i -> {
            final int width = widths[i];
            final int length = headers[i].length();
            final int leading = (width - length) / 2;
            final int trailing = width - (length + leading);
            final StringBuilder text = new StringBuilder();
            whitespace(text, leading + 1);
            text.append("%").append(i + 1).append("$s");
            whitespace(text, trailing);
            text.append("  |");
            return text.toString();
        }).reduce((left, right) -> left + " " + right).orElse("");
    }

    /**
     * Returns the jandas template given the widths specified
     *
     * @param widths the token widths
     * @return the line format template
     */
    private static String getDataTemplate(int[] widths) {
        return IntStream.range(0, widths.length)
                .mapToObj(i -> " %" + (i + 1) + "$" + widths[i] + "s  |")
                .reduce((left, right) -> left + " " + right)
                .orElse("");
    }

    /**
     * Returns a whitespace string of the rowCount specified
     *
     * @param length the rowCount for whitespace
     */
    private static void whitespace(StringBuilder text, int length) {
        IntStream.range(0, length).forEach(i -> text.append(" "));
    }

    /**
     * Prints the specified DataFrame to the stream bound to this printer
     *
     * @param frame the DataFrame to print
     */
    public void print(DataFrame frame) {
        try {
            final String[] headers = getHeaderTokens(frame);
            final String[][] data = getDataTokens(frame);
            final int[] widths = getWidths(headers, data);
            final String dataTemplate = getDataTemplate(widths);
            final String headerTemplate = getHeaderTemplate(widths, headers);
            final int totalWidth = IntStream.of(widths).map(w -> w + 5).sum() - 1;
            final int totalHeight = data.length + 1;
            int capacity = totalWidth * totalHeight;
            if (capacity < 0) {
                capacity = 0;
            }
            final StringBuilder text = new StringBuilder(capacity);
            text.append(tableName(frame, totalWidth)).append("\n");
            final String headerLine = String.format(headerTemplate, (Object[]) headers);
            text.append(headerLine).append("\n");
            for (int j = 0; j < totalWidth; j++) {
                text.append("-");
            }
            for (String[] row : data) {
                final String dataLine = String.format(dataTemplate, (Object[]) row);
                text.append("\n");
                text.append(dataLine);
            }
            final byte[] bytes = text.toString().getBytes();
            this.stream.write(bytes);
            this.stream.flush();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to print DataFrame", ex);
        }
    }

    private String tableName(DataFrame frame, int width) {
        if (frame.name().length() > width) {
            return frame.name();
        }
        int diff = width - frame.name().length();
        String result = StringUtils.repeat(" ", diff / 2) + frame.name();
        return result + StringUtils.repeat(" ", width - result.length());
    }

    /**
     * Returns the header string tokens for the frame
     *
     * @param frame the frame to create header tokens
     * @return the header tokens
     */
    private String[] getHeaderTokens(DataFrame frame) {
        final int colCount = frame.columnCount();
        final String[] header = new String[colCount];
        IntStream.range(0, colCount).forEach(colIndex -> {
            header[colIndex] = frame.column(colIndex).name();
        });
        return header;
    }


    private static void addHeadTokens(String[][] data, int rowCount, int colCount, int start, DataFrame df){
        for (int i = start; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                String value = df.getString(i, j);
                data[i][j] = value == null ? "" : value;
            }
        }
    }

    private static void addTailTokens(String[][] data, int rowCount, int colCount, int start, int maxRows, DataFrame df){
        for (int i=start; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                String value = df.getString(df.rowCount() - maxRows + i, j);
                data[i][j] = value == null ? "" : value;
            }
        }
    }

    private static void addSeparator(String[][] data, int i, int colCount, DataFrame df){
        for (int j = 0; j < colCount; j++) {
            data[i][j] = "...";
        }
    }

    private String[][] getDataTokens(DataFrame frame) {
        if (frame.rowCount() == 0) return new String[0][0];
        final int colCount = frame.columnCount();
        final int rowCount= Math.min(maxRows, frame.rowCount());
        final String[][] data = new String[rowCount][colCount];
        switch (printType) {
            case BOTH:
                if (frame.rowCount() > maxRows) {
                    int centre = rowCount/2;
                    addHeadTokens(data, centre, colCount, 0, frame);
                    addSeparator(data, centre, colCount, frame);
                    addTailTokens(data, rowCount, colCount, centre+1, maxRows, frame);

                } else {
                    addHeadTokens(data, rowCount, colCount, 0, frame);
                }
                break;
            case HEAD:
                addHeadTokens(data, rowCount, colCount, 0, frame);
                break;
            case TAIL:
                int start = frame.rowCount() - maxRows;
                addTailTokens(data, rowCount, colCount, start , maxRows, frame);
                break;
        }

        return data;
    }


}

