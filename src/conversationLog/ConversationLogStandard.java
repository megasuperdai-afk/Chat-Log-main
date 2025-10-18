package conversationLog;
// This copied from OSU components.standard.Standard on the website; I just put it here to make the Kernel not marking errors.
// To be honest, I don't really understand what this file is for.
// I not 100% understand the purpose of this file, but it seems to be legit as when I checked the OSU components website,
// this file is indeed part of their standard component library. So I will keep it here to avoid errors in the Kernel interface.
// Also the format of all file I did in this exercise is same with OSU components website, I look in Queue kernel, queue1l and queue
// and standard interface to mimic their format.
public interface ConversationLogStandard<T> {
 
    /**
     * Returns a new object with the same <i>dynamic</i> type as {@code this},
     * having an initial value.
     *
     * If the type {@code T} has a no-argument constructor, then the value of
     * the new returned object satisfies the contract of the no-argument
     * constructor for {@code T}. If {@code T} does not have a no-argument
     * constructor, then the value of the new returned object satisfies the
     * contract of the constructor call that was used to initialize {@code this}
     * .
     *
     * @return new object "like" {@code this} with an initial value
     * @ensures is_initial(newInstance)
     */
    T newInstance();
 
    /**
     * Resets {@code this} to an initial value.
     *
     * If the type {@code T} has a no-argument constructor, then {@code this}
     * satisfies the contract of the no-argument constructor for {@code T}. If
     * {@code T} does not have a no-argument constructor, then {@code this}
     * satisfies the contract of the constructor call that was used to
     * initialize {@code #this}.
     *
     * @clears this
     */
    void clear();
 
    /**
     * Sets {@code this} to the incoming value of {@code source}, and resets
     * {@code source} to an initial value; the declaration notwithstanding, the
     * <i>dynamic</i> type of {@code source} must be the same as the
     * <i>dynamic</i> type of {@code this}.
     *
     * If the type {@code T} has a no-argument constructor, then {@code source}
     * satisfies the contract of the no-argument constructor for {@code T}. If
     * {@code T} does not have a no-argument constructor, then {@code source}
     * satisfies the contract of the constructor call that was used to
     * initialize {@code #source}.
     *
     * @param source
     *            object whose value is to be transferred
     * @replaces this
     * @clears source
     * @ensures this = #source
     */
    void transferFrom(T source);
 
}