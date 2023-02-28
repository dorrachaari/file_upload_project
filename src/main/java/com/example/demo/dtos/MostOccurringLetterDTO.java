package com.example.demo.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MostOccurringLetterDTO extends FileInfoDTO {
    private String mostOccurringLetter ;
}
