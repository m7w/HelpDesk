import apiService from "./apiService";

const getTickets = (inputParams) => {
    var query = "/api/tickets";

    const { searchColumn, searchString, ...allButSearch } = inputParams;
    const params = new URLSearchParams(allButSearch);

    [...params.entries()].forEach(([key, value]) => {
        if (value === 'undefined' || value === 'null' || !value) {
            params.delete(key);
        }
    });

    const cleanedParams = String(params);
    if (cleanedParams) {
        query = query + "?" + params;
    }

    if (searchString !== 'undefined' && searchString !== 'null' && searchString) {
        query = query + "&search=" + searchColumn + "==" + encodeURIComponent(searchString);
    }

    console.log("query: " + query);
    return apiService.get(query);
};

const ticketService = {
    getTickets,
}

export default ticketService;
