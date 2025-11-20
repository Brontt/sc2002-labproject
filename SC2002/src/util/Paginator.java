package util;

import java.util.List;

public class Paginator<T> {
    private final List<T> list; private final int size;
    public Paginator(List<T> list, int size) { this.list=list; this.size=Math.max(1,size); }
    public int totalPages(){ return (list.size()+size-1)/size; }
    public List<T> page(int p){
        int total = totalPages(); if (total==0) return List.of();
        if (p<1) p=1; if (p>total) p=total;
        int from = (p-1)*size, to = Math.min(from+size, list.size());
        return list.subList(from, to);
    }
}
