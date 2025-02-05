package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEvent;

import com.example.samuraitravel.entity.User;

import lombok.Getter;

@Getter  //外部（具体的にはListenerクラス）からそれらの情報を取得できるようにゲッターを定義する

public class SignupEvent extends ApplicationEvent {
    private User user;
    private String requestUrl;        

    public SignupEvent(Object source, User user, String requestUrl) {
        super(source); //スーパークラス（親クラス）のコンストラクタを呼び出し、イベントのソース（発生源）を渡す
        
        this.user = user;        
        this.requestUrl = requestUrl;
    }

}
