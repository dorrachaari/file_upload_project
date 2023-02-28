package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class BackwardLinesListDTO extends AbstractDTO{
    private List<BackwardLineDTO> list;

    public BackwardLinesListDTO() {
        this.list = new ArrayList<>();
    }

    public void addItem(BackwardLineDTO item) {
        this.list.add(item);
    }
}
