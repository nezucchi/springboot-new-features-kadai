package com.example.samuraitravel.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.service.VerificationTokenService;

@Component  //ListenerクラスのインスタンスがDIコンテナに登録されるようにする

public class SignupEventListener {
	private final VerificationTokenService verificationTokenService;    
    private final JavaMailSender javaMailSender;
    
    public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
        this.verificationTokenService = verificationTokenService;        
        this.javaMailSender = mailSender;
    }

    @EventListener  //Listenerクラス内では、イベント発生時に実行したいメソッドに対して@EventListenerアノテーションをつける
    private void onSignupEvent(SignupEvent signupEvent) {
    	//通知を受け付けるEventクラスをメソッドの引数に設定する
        User user = signupEvent.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.create(user, token);
        	//生成したトークンはユーザーIDとともにデータベースに保存する
        String recipientAddress = user.getEmail();
        String subject = "メール認証";
        String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
        String message = "以下のリンクをクリックして会員登録を完了してください。";
        
        SimpleMailMessage mailMessage = new SimpleMailMessage(); 
        mailMessage.setTo(recipientAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(message + "\n" + confirmationUrl);
        javaMailSender.send(mailMessage);
        //avaMailSenderインターフェースのsend()メソッドに前述のSimpleMailMessageオブジェクトを渡すだけでメール送信できる
    }

}
