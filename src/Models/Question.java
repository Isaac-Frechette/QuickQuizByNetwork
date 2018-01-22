package Models;

public class Question {
    
    private int QuestionNumber;
    private String Topic;
    private String Question;
    private String AnswerA;
    private String AnswerB;
    private String AnswerC;
    private String AnswerD;
    private String CorrectAnswer;

    public Question() {

    }

    public Question(int QnNum, String topic, String Qn, String AnsA, String AnsB, String AnsC, String AnsD, String TrueAns) {
        QuestionNumber = QnNum;
        Topic = topic;
        Question = Qn;
        AnswerA = AnsA;
        AnswerB = AnsB;
        AnswerC = AnsC;
        AnswerD = AnsD;
        CorrectAnswer = TrueAns;
    }
    
    //getters and setters below here
    
    
        public int getQuestionNumber() {
        return QuestionNumber;
    }

    public void setQuestionNumber(int QuestionNumber) {
        this.QuestionNumber = QuestionNumber;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String Topic) {
        this.Topic = Topic;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }

    public String getAnswerA() {
        return AnswerA;
    }

    public void setAnswerA(String AnswerA) {
        this.AnswerA = AnswerA;
    }

    public String getAnswerB() {
        return AnswerB;
    }

    public void setAnswerB(String AnswerB) {
        this.AnswerB = AnswerB;
    }

    public String getAnswerC() {
        return AnswerC;
    }

    public void setAnswerC(String AnswerC) {
        this.AnswerC = AnswerC;
    }

    public String getAnswerD() {
        return AnswerD;
    }

    public void setAnswerD(String AnswerD) {
        this.AnswerD = AnswerD;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String CorrectAnswer) {
        this.CorrectAnswer = CorrectAnswer;
    }
}
