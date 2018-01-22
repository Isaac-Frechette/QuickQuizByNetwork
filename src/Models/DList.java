package Models;

public class DList {
    DListNode head;

    public DList()
    {
        head = new DListNode();
    }

    public DListNode findByID(String id)
    {          // find DListNode containing x
        for (DListNode current = head.next; current != head; current = current.next)
        {
            if (current.getQuestionNumber().equals(id))
            {        // is x contained in current DListNode?
                System.out.println("Question " + id + " found");
                return current;       // return DListNode containing x
            }
        }
        System.out.println("Question " + id + " not found");
        return null;
    }

    //This Get method Added by Matt C
    public DListNode get(int i)
    {
        DListNode current = this.head;
        if (i < 0 || current == null)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        while (i > 0)
        {
            i--;
            current = current.next;
            if (current == null)
            {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        return current;
    }

    public String print()
    {
        String nodeList = "HEAD";
        if (head.next == head)
        {
            return " List empty";
        }
        
        for (DListNode current = head.next; current != head && current != null; current = current.next)
        {
            nodeList += (" <-> " + current.getQuestionTopic() + "," + current.getQuestionNumber());
        }
        nodeList += " <-> TAIL";
        return nodeList;
    }
    
    public void out()
    {                  // print content of list
        if (head.next == head)
        {             // list is empty, only header Node
            System.out.println("list empty");
            return;
        }
        System.out.print("list content = ");
        
        for (DListNode current = head.next; current != head; current = current.next)
        {
            System.out.print(" " + current.getQuestionNumber());
        }
    }

    public DListNode getHead()
    {
        return head;
    }
}
