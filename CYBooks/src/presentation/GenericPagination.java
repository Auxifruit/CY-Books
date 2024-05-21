package presentation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

public class GenericPagination<T> {
    private Pagination pagination;
    private TableView<T> tableView;
    private ObservableList<T> data;
    private int itemsPerPage;

    public GenericPagination(TableView<T> tableView, ObservableList<T> data, int itemsPerPage) {
        this.tableView = tableView;
        this.data = data;
        this.itemsPerPage = itemsPerPage;

        // Initialize pagination
        pagination = new Pagination((data.size() / itemsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> changeTableView(newValue.intValue(), itemsPerPage));
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, data.size());
        tableView.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
    }

    private TableView<T> createPage(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, data.size());
        tableView.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
        return tableView;
    }

    public Pagination getPagination() {
        return pagination;
    }
}