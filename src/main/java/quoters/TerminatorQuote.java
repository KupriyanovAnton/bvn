package quoters;

import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

import javax.annotation.PostConstruct;

@Profiling
public class TerminatorQuote implements Quote {
    private String message;

    public TerminatorQuote() {
        System.out.println("Phase 1");
    }

    @PostConstruct
    public void init(){
        System.out.println("Phase 2");
    }


    public void setMessage(String message) {
        this.message = message;
    }

    @PostProxy
    public void sayQuote() {
        System.out.println("Phase 3");
        System.out.println("message - " + message);
    }
}

