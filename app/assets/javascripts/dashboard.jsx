var UserStockList = React.createClass({
    loadStocks: function() {
        $.ajax({
            url: "/stock/user/testId",
            dataType: 'json',
            cache: false,
            success: function(data) {
                this.setState({data: data});
            }.bind(this),
            error: function(xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    getInitialState: function() {
        return {data: []};
    },
    componentDidMount: function() {
        this.loadStocks();
        setInterval(this.loadStocks, 2000);
    },
    render: function() {
        return (
            <div className="">
                <h3>Sell Stocks</h3>
                <Stocks data={this.state.data} />
            </div>
        );
    }
});

var StockStatus = React.createClass({
    loadComputeResult: function() {
        $.ajax({
            url: "/transaction/status",
            dataType: 'json',
            cache: false,
            success: function(data) {
                this.setState({data: data});
            }.bind(this),
            error: function(xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    getInitialState: function() {
        return {data: []};
    },
    componentDidMount: function() {
        this.loadComputeResult();
        setInterval(this.loadComputeResult, 1000);
    },
    render: function() {
        return (
            <div className="stock-status">
                <h3>Stock Status</h3>
                <ComputeResult data={this.state.data} />
                <a href="/reset">Reset transactions</a>
            </div>
        );
    }
});

var ComputeResult = React.createClass({
    render: function() {
        var nodes = this.props.data.map(function (computeResult) {
            var redgreenHighlight = "";
            if (("" + computeResult.totalBalance).contains("-")) {
                redgreenHighlight = "red";
            } else {
                redgreenHighlight = "green";
            }
            return (
                <Reactable.Tr>
                    <Reactable.Td column="Name" data={computeResult.name} />
                    <Reactable.Td className={redgreenHighlight}  column="Status" data={'$' + computeResult.totalBalance}></Reactable.Td>
                </Reactable.Tr>
            );

        });

            return (
                <Reactable.Table className="table">
                    {nodes}
                </Reactable.Table>
            );

    }
});

var Currencies = React.createClass({
    loadCurrencies: function() {
        $.ajax({
            url: "/currency",
            dataType: 'json',
            cache: false,
            success: function(data) {
                this.setState({data: data});
            }.bind(this),
            error: function(xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    getInitialState: function() {
        return {data: []};
    },
    componentDidMount: function() {
        this.loadCurrencies();
        //setInterval(this.loadStocks, this.props.pollInterval);
    },
    render: function() {
        return (
            <div className="">
                <h3>Currency Rate</h3>
                <Currency data={this.state.data} />
                <a href="/view/dashboard">Generate new rates</a>
            </div>
        );
    }
});

var Currency = React.createClass({
    render: function() {
        var currencyNodes = this.props.data.map(function (currency) {
            return (
                <Reactable.Tr>
                    <Reactable.Td column="Name" data={currency.name} />
                    <Reactable.Td  column="Rate" className={currency.name} data={currency.rate}/>
                </Reactable.Tr>
            );
        });
        return (
            <Reactable.Table className="table">
                {currencyNodes}
            </Reactable.Table>
        );
    }
});

var Stocks = React.createClass({
    render: function() {
        var stockNodes = this.props.data.map(function (stock) {
            var stockTradeDate = $.datepicker.formatDate("dd-M-yy", new Date(stock.tradeDate));
            var rate = $('.' + stock.currency.name).text()
            return (
                <Reactable.Tr>
                    <Reactable.Td column="Trade Date" data={stockTradeDate} />
                    <Reactable.Td column="Name" data={stock.name}/>
                    <Reactable.Td column="Quantity" data={stock.quantity}/>
                    <Reactable.Td column="Currency" data={stock.currency.name}/>
                    <Reactable.Td column="Price" data={stock.price}/>
                    <Reactable.Td column=""><a href={'/view/stock/'+stock._id+'/sell/' + rate }>SELL</a></Reactable.Td>
                </Reactable.Tr>
            );
        });
        if (stockNodes.length == 0) {
            return (
                <h4>Nothing to sell yet. <a href="/view/stock">Buy here.</a></h4>
            );
        } else {
            return (
                <Reactable.Table className="table" itemsPerPage={10} sortable={true} filterable={['Name', 'Trade Date']}>
                    {stockNodes}
                </Reactable.Table>
            );
        }

    }
});

React.render(<Currencies url="/view/dashboard" />, document.getElementById('currency'));
React.render(<StockStatus url="/view/dashboard" />, document.getElementById('stockStatus'));
React.render(<UserStockList url="/view/dashboard" />, document.getElementById('sellStocks'));
