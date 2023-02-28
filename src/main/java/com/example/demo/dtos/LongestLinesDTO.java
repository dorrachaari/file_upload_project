package com.example.demo.dtos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LongestLinesDTO extends AbstractDTO{
    private List<String> list;

    public LongestLinesDTO() {
        this.list = new ArrayList<>();
    }

    public void addAll(List<String> lines){
        this.list.addAll(lines);
    }

}
