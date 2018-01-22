package Models;

public class DListNode {
    DListNode prev;              // previous Node in a doubly-linked list
    DListNode next;              // next Node in a doubly-linked list
    String questionTopic;
    String questionNumber;
    //public char data;       // data stored in this Node

    public DListNode()
    {                // constructor for head Node 
        prev = this;           // of an empty doubly-linked list
        next = this;
        questionTopic = "HEAD";
        questionNumber = "";
        // not used except for printing data in list head
    }

    public DListNode(String qTopic, String qNumber)
    {       // constructor for a Node with data
        prev = null;
        next = null;
        questionTopic = qTopic;
        questionNumber = qNumber;
        //this.data = data;     // set argument data to instance variable data
    }

    public void append(DListNode newNode)
    {  // attach newNode after this Node
        newNode.prev = this;
        newNode.next = next;
        if (next != null)
        {
            next.prev = newNode;
        }
        next = newNode;
    }

    public void insert(DListNode newNode)
    {  // attach newNode before this Node
        newNode.prev = prev;
        newNode.next = this;
        prev.next = newNode;;
        prev = newNode;
    }

    public void remove()
    {              // remove this Node
        next.prev = prev;                 // bypass this Node
        prev.next = next;
    }

    public String getQuestionTopic()
    {
        return questionTopic;
    }

    public void setQuestionTopic(String questionTopic)
    {
        this.questionTopic = questionTopic;
    }

    public String getQuestionNumber()
    {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber)
    {
        this.questionNumber = questionNumber;
    }

    public DListNode getPrev()
    {
        return prev;
    }

    public DListNode getNext()
    {
        return next;
    }

    public void setNext(DListNode next)
    {
        this.next = next;
    }
    
}
