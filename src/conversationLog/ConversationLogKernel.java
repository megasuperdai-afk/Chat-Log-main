package conversationLog;

import components.map.Map;
import components.standard.Standard;

/**
 * Kernel interface for ConversationLog .
 */
public interface ConversationLogKernel extends Standard<ConversationLog> {

    /**
     * Report the number of chat entries in the log.
     *
     * @return the number of chat entries
     */
    int length();

    /**
     * Check if the log is empty.
     *
     * @return true if the log is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Append a chat entry to the log.
     *
     * @param speaker
     *            non-null/non-empty
     * @param text
     *            non-null
     */
    void append(String speaker, String text);

    /**
     * Undo chat: removes the last entry and reports it as a map with keys
     * "speaker", "text".
     *
     * @return removed entry encoded as a Map<String,String>
     * @requires |this| > 0
     */
    Map<String, String> removeChat();

    /**
     * Reset the conversation log to empty.
     *
     * @clear this
     */
    void reset();

    /**
     * Keeps only entries in [start, end) and deletes all other chat.
     *
     * @param start
     *            inclusive index
     * @param end
     *            exclusive index (0 ≤ start ≤ end ≤ |#this|)
     * @updates this
     * @return this (now containing only the chosen segment)
     */
    ConversationLog segment(int start, int end);
}
