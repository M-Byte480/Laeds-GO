package ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces;

public interface ICollectionConnection {
    public void push(IDocument item) throws InterruptedException;

    public IDocument get(int id);
}
