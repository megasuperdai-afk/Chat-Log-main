package conversationLog;
import components.standard.Standard;
import components.map.Map;

public interface ConversationLogKernel extends Standard<ConversationLog> {

    // Report number of chat entries in the log.
    int length();


    // Check if the log is empty.
    boolean isEmpty();

    /**
    * Append a chat entry to the log.
    * @param speaker non-null/non-empty
    * @param text non-null
    */
    void append(String speaker, String text);


    /**
    * Undo chat: removes the last entry and reports it as a map with keys
    * "speaker", "text".
    * @return removed entry encoded as a Map<String,String>
    * @requires |this| > 0
    */
    Map<String, String> removeChat();

    // Resets the chat log to empty, this is same with clear().`
    void reset();


    /**
    * Keeps only entries in [start, end) and deletes all other chat.
    * @param start inclusive index
    * @param end exclusive index (0 ≤ start ≤ end ≤ |#this|)
    * @updates this
    * @return this (now containing only the chosen segment)
    */
    ConversationLog segment(int start, int end);
}
