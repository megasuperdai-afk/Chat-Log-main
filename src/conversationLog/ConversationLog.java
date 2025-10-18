package conversationLog;

import components.sequence.Sequence;
import components.simplereader.SimpleReader;
import components.simplewriter.SimpleWriter;


// Secondary interface for ConversationLog
// The file name should be secondary, but when I take reference from OSU components website, there is method in normal also, what I 
//mean is like queuekernel => queue => queueSecondary => queue1L; but in the first homework of this portfolio does not have the one
// between kernel and secondary, so I just put kernel => normal => secondary
public interface ConversationLog extends ConversationLogKernel {

    // export/import chat log
    void exportChat(SimpleWriter out);
    void importChat(SimpleReader in);

    // Convert the entire conversation log to a transcript string
    String toTranscript();

    // Search for substring in the chat log, return the list of indices where found
    Sequence<Integer> find(String substring);

    // Get all chats from a specific speaker
    Sequence<String> speakerChat(String speaker);
}