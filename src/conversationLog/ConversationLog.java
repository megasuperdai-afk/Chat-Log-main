package conversationLog;

import components.sequence.Sequence;
import components.simplereader.SimpleReader;
import components.simplewriter.SimpleWriter;

//The file name should be secondary, but when I take reference from OSU
//components website, there is method in normal also, what I
//mean is like queuekernel => queue => queueSecondary => queue1L; but in the
//first homework of this portfolio does not have the one
//between kernel and secondary, so I just put kernel => normal => secondary

/**
 * ConversationLog interface.
 */
public interface ConversationLog extends ConversationLogKernel {

    // export/import chat log
    /**
     * Export the chat log to a SimpleWriter.
     *
     * @param out
     *            the SimpleWriter to write to
     */
    void exportChat(SimpleWriter out);

    /**
     * Import the chat log from a SimpleReader.
     *
     * @param in
     *            the SimpleReader to read from
     */
    void importChat(SimpleReader in);

    // Convert the entire conversation log to a transcript string
    /**
     * Convert the entire conversation log to a transcript string.
     *
     * @return the transcript string
     */
    String toTranscript();

    // Search for substring in the chat log, return the list of indices where found
    /**
     * Search for a substring in the chat log and return the list of indices
     * where found.
     *
     * @param substring
     *            the substring to search for
     * @return a sequence of indices where the substring is found
     */
    Sequence<Integer> find(String substring);

    // Get all chats from a specific speaker
    /**
     * Get all chats from a specific speaker.
     *
     * @param speaker
     *            the speaker whose chats to retrieve
     * @return a sequence of chats from the specified speaker
     */
    Sequence<String> speakerChat(String speaker);
}
