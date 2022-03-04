import apiService from "./apiService";

const getTickets = (page, size, orderBy, order, searchColumn, searchPattern) => {
    var query = "/api/tickets";
    const params = new URLSearchParams({
        page: page,
        size: size,
        order_by: orderBy,
        order: order,
    });

    [...params.entries()].forEach(([key, value]) => {
        if (value === 'undefined' || value === 'null' || !value) {
            params.delete(key);
        }
    });

    const cleanedParams = String(params);
    if (cleanedParams) {
        query = query + "?" + params;
    }

    if (searchPattern !== 'undefined' && searchPattern !== 'null' && searchPattern) {
        query = query + "&search=" + searchColumn + "==" + searchPattern;
    }

    console.log("query: " + query);
    return apiService.get(query);
};

const ticketService = {
    getTickets,
}

export default ticketService;
