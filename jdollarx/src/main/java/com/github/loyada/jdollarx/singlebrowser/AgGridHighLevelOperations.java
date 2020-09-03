package com.github.loyada.jdollarx.singlebrowser;

import com.github.loyada.jdollarx.Path;

import static com.github.loyada.jdollarx.ElementProperties.hasAggregatedTextEqualTo;
import static com.github.loyada.jdollarx.singlebrowser.custommatchers.CustomMatchers.isPresent;
import static java.util.Collections.EMPTY_LIST;
import java.util.List;

import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.hoverOver;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;

public final class AgGridHighLevelOperations {
    final private Path gridContainer;

    public AgGridHighLevelOperations(Path gridContainer){
        this.gridContainer = gridContainer;
    }

    public AgGrid buildMinimalGridFromHeader(List<String> headers) {
        return AgGrid.getBuilder()
                .withHeaders(headers)
                .withRowsAsStrings(EMPTY_LIST)
                .containedIn(gridContainer)
                .build();
    }

    public void ensureCellValueIsPresent(int rowNumber, String columnTitle, String expectedValue) {
        AgGrid grid = buildMinimalGridFromHeader(singletonList(columnTitle));
        Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(rowNumber, columnTitle);
        assertThat(cell.that(hasAggregatedTextEqualTo(expectedValue)), isPresent());
    }

    public Path hoverOverCell(int rowNumber, String columnTitle) {
        AgGrid grid = buildMinimalGridFromHeader(singletonList(columnTitle));
        Path cell = grid.ensureVisibilityOfRowWithIndexAndColumn(rowNumber, columnTitle);
        hoverOver(cell);
        return cell;
    }

}
