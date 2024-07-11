package com.example.demo.entity;


import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String imgName;

    private String path;

    private boolean isThumbnail;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;


    public static Image createImage(String uuid, String imgName, String path, Question question, boolean isThumbnail) {
        Image image = new Image();
        image.uuid = uuid;
        image.imgName = imgName;
        image.path = path;
        image.question = question;
        image.isThumbnail = isThumbnail;
        return image;
    }

}
