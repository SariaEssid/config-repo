package com.javainuse.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ESSID Saria
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EntityDto implements Serializable
{

    private Integer id;
    private String answer;

    public EntityDto(Integer id, String answer) {
        this.id = id;
        this.answer = answer;
    }

}
