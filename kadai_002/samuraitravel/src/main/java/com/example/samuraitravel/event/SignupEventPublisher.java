package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;

@Component  //@ComponentアノテーションをつけてDIコンテナに登録し、呼び出すクラス（今回はコントローラ）に対して依存性の注入（DI）を行えるようにする

public class SignupEventPublisher {
 private final ApplicationEventPublisher applicationEventPublisher;
     
     public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
         this.applicationEventPublisher = applicationEventPublisher;                
     }
     //イベントを発行するには、ApplicationEventPublisherインターフェースが提供するpublishEvent()メソッドを使う
     //引数には発行したいEventクラス（今回はSignupEventクラス）のインスタンスを渡す
     public void publishSignupEvent(User user, String requestUrl) {
         applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
         //SignupEventクラスのコンストラクタの第1引数には、自分自身（SignupEventPublisherクラス）のインスタンスを渡している
         //SignupEventクラスには、このインスタンスがイベントのソース（発生源）として渡される
     }

}
