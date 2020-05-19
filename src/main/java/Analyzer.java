
public interface Analyzer<T> {

    boolean check(T content);

    TypeMessage getContentType();

    FilterType getFilterType();
}
