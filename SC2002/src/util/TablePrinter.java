package util;

import java.util.*;
import java.util.stream.Collectors;

public final class TablePrinter {
    public enum Align { LEFT, CENTER, RIGHT }

    public static final class Column {
        final String header;
        final Align align;
        final int minWidth, maxWidth;
        int width; // computed

        Column(String header, Align align, int minWidth, int maxWidth) {
            this.header = header; this.align = align;
            this.minWidth = minWidth; this.maxWidth = Math.max(minWidth, maxWidth);
            this.width = minWidth;
        }
    }

    public static final class Builder {
        private final List<Column> cols = new ArrayList<>();
        private final List<List<String>> rows = new ArrayList<>();
        private boolean unicodeBorders = false;
        private int maxTableWidth = 120;

        public Builder addColumn(String header, Align a, int minW, int maxW) {
            cols.add(new Column(header, a, minW, maxW)); return this;
        }
        public Builder unicodeBorders(boolean b) { this.unicodeBorders = b; return this; }
        public Builder maxTableWidth(int w) { this.maxTableWidth = Math.max(40, w); return this; }

        public TablePrinter build() { return new TablePrinter(cols, rows, unicodeBorders, maxTableWidth); }
    }
    public static Builder builder() { return new Builder(); }

    private final List<Column> cols;
    private final List<List<String>> rows;
    private final boolean unicode;
    private final int maxWidth;

    private TablePrinter(List<Column> cols, List<List<String>> rows, boolean unicode, int maxWidth) {
        this.cols = cols; this.rows = rows; this.unicode = unicode; this.maxWidth = maxWidth;
    }

    public void addRow(Object... cells) {
        List<String> r = new ArrayList<>(cols.size());
        for (int i = 0; i < cols.size(); i++) {
            r.add(i < cells.length && cells[i] != null ? String.valueOf(cells[i]) : "");
        }
        rows.add(r);
    }

    public String render() {
        computeWidths();
        final String h = border("top");
        final String m = border("mid");
        final String f = border("bot");
        StringBuilder sb = new StringBuilder();
        sb.append(h).append('\n');
        sb.append(line(cols.stream().map(c -> c.header).collect(Collectors.toList()))).append('\n');

        sb.append(m).append('\n');
        for (int i = 0; i < rows.size(); i++) {
            sb.append(line(rows.get(i))).append('\n');
        }
        sb.append(f);
        return sb.toString();
    }

    private void computeWidths() {
        // initial: header width
        for (Column c : cols) c.width = Math.max(c.minWidth, Math.min(c.maxWidth, c.header.length()));
        // grow with cell contents (bounded by maxWidth per column)
        for (List<String> r : rows) {
            for (int i = 0; i < cols.size(); i++) {
                Column c = cols.get(i);
                int need = Math.min(c.maxWidth, (r.get(i) != null ? r.get(i).length() : 0));
                c.width = Math.max(c.width, Math.max(c.minWidth, Math.min(need, c.maxWidth)));
            }
        }
        // shrink to meet table max width if necessary (greedy)
        int table = tableWidth();
        while (table > maxWidth) {
            // find the widest column that still can shrink
            Column widest = null;
            for (Column c : cols) if (c.width > c.minWidth) {
                if (widest == null || c.width > widest.width) widest = c;
            }
            if (widest == null) break; // cannot shrink
            widest.width--;
            table = tableWidth();
        }
    }

    private int tableWidth() {
        // sum of col widths + borders/separators
        int sum = 1; // left border
        for (Column c : cols) sum += c.width + 3; // space + sep for each col
        return sum; // rough, fine for limiting
    }

    private String border(String kind) {
        String[] a = unicode
            ? new String[]{"┌","┬","┐","├","┼","┤","└","┴","┘","─","│"}
            : new String[]{"+","+","+","+","+","+","+","+","+", "-", "|"};
        String left = kind.equals("top") ? a[0] : kind.equals("mid") ? a[3] : a[6];
        String cross= kind.equals("top") ? a[1] : kind.equals("mid") ? a[4] : a[7];
        String right= kind.equals("top") ? a[2] : kind.equals("mid") ? a[5] : a[8];
        String dash = a[9];

        StringBuilder sb = new StringBuilder(left);
        for (int i = 0; i < cols.size(); i++) {
            sb.append(repeat(dash, cols.get(i).width + 2));
            sb.append(i == cols.size()-1 ? right : cross);
        }
        return sb.toString();
    }

    private String line(List<String> cells) {
        String sep = unicode ? "│" : "|";
        StringBuilder sb = new StringBuilder(sep);
        for (int i = 0; i < cols.size(); i++) {
            Column c = cols.get(i);
            String cell = i < cells.size() && cells.get(i) != null ? cells.get(i) : "";
            cell = fit(cell, c.width);
            sb.append(' ').append(pad(cell, c.width, c.align)).append(' ').append(sep);
        }
        return sb.toString();
    }

    private static String fit(String s, int w) {
        if (s.length() <= w) return s;
        if (w <= 1) return s.substring(0, w);
        return s.substring(0, Math.max(0, w - 1)) + "…";
    }

    private static String pad(String s, int w, Align a) {
        int space = w - s.length(); if (space <= 0) return s;
        switch (a) {
            case RIGHT: return repeat(" ", space) + s;
            case CENTER:
                int left = space / 2, right = space - left;
                return repeat(" ", left) + s + repeat(" ", right);
            default: return s + repeat(" ", space);
        }
    }

    private static String repeat(String ch, int n) {
        return new String(new char[Math.max(0,n)]).replace("\0", ch);
    }
}
