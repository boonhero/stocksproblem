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
                <h3>Stocks Overview</h3>
                <ComputeResult data={this.state.data} />
            </div>
        );
    }
});

var ComputeResult = React.createClass({
    render: function() {
        var nodes = this.props.data.map(function (computeResult) {
            var redgreenHighlight = "";
            if (computeResult.totalBalance <= 0) {
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


var DateStockList = React.createClass({
    loadStocks: function(date) {
        $.ajax({
            url: "/stockdate/" + date,
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
        return {date: "01-07-2015", data: []};
    },
    componentDidMount: function() {
        var self = this;
        $(function () {
            $("#datetimepicker1").datepicker({defaultDate: $.datepicker.parseDate('yymmdd', '20150701'), dateFormat: 'dd-mm-yy'});
            $("#loadStockButton").click(function() {
                var date = $("#datetimepicker1").val();
                $.ajax({
                    url: "/stockdate/" + date,
                    dataType: 'json',
                    cache: false,
                    success: function(data) {
                        self.setState({data: data, date: date});
                    }.bind(self),
                    error: function(xhr, status, err) {
                        console.error(self.props.url, status, err.toString());
                    }.bind(self)
                });
            });
        });
        this.loadStocks(this.state.date);
    },
    render: function() {
        var submit = this.loadStocksByState
        return (
            <div className="">
                <h3>Stocks today! {this.state.date}</h3>
                <TodayStocks data={this.state.data} />
                <form>
                    <input type="text" id="datetimepicker1" placeholder="Choose date" required="true" value={this.state.date} />
                    <br/><input id="loadStockButton" type="button" value="Load" /><span> Used also in selling stocks.</span>
                </form>
            </div>
        );
    }
});

//var Currencies = React.createClass({
//    loadCurrencies: function() {
//        $.ajax({
//            url: "/currency",
//            dataType: 'json',
//            cache: false,
//            success: function(data) {
//                this.setState({data: data});
//            }.bind(this),
//            error: function(xhr, status, err) {
//                console.error(this.props.url, status, err.toString());
//            }.bind(this)
//        });
//    },
//    loadCurrenciesByDate: function(e) {
//        e.preventDefault();
//        $.ajax({
//            url: "/currency/" + $("#datetimepicker1").value,
//            dataType: 'json',
//            cache: false,
//            success: function(data) {
//                this.setState({data: data});
//            }.bind(this),
//            error: function(xhr, status, err) {
//                console.error(this.props.url, status, err.toString());
//            }.bind(this)
//        });
//    },
//    getInitialState: function() {
//        return {data: []};
//    },
//    componentDidMount: function() {
//        $(function() {
//            $("#datetimepicker1").datepicker({ defaultDate: $.datepicker.parseDate('yymmdd', '20150701') });
//        });
//        this.loadCurrencies();
//        //setInterval(this.loadStocks, this.props.pollInterval);
//    },
//    render: function() {
//        return (
//            <div className="">
//                <h3>Currency Rate</h3>
//                <Currency data={this.state.data} />
//                <form>
//                    <input type="date" id="datetimepicker1" placeholder="Choose date" required="true" />
//                    <input type="button" value="Select date" onClick={this.loadCurrenciesByDate}/>
//                </form>
//            </div>
//        );
//    }
//});
//
//var Currency = React.createClass({
//    render: function() {
//        var currencyNodes = this.props.data.map(function (currency) {
//            return (
//                <Reactable.Tr>
//                    <Reactable.Td column="Name" data={currency.name} />
//                    <Reactable.Td  column="Rate" className={currency.name} data={currency.rate}/>
//                </Reactable.Tr>
//            );
//        });
//        return (
//            <Reactable.Table className="table">
//                {currencyNodes}
//            </Reactable.Table>
//        );
//    }
//});

var TodayStocks = React.createClass({
    render: function() {
        var stockNodes = this.props.data.map(function (stock) {
            var stockTradeDate = $.datepicker.formatDate("dd-M-yy", new Date(stock.tradeDate));
            var rate = $('.' + stock.currency.name).text()
            return (
                <Reactable.Tr>
                    <Reactable.Td column="Trade Date" data={stockTradeDate} />
                    <Reactable.Td column="Name" data={stock.name}/>
                    <Reactable.Td column="Currency" data={stock.currency.name}/>
                    <Reactable.Td column="Rate" data={stock.currency.rate}/>
                    <Reactable.Td column="Price" data={stock.price}/>
                    <Reactable.Td column=""><a href={'/view/stock/buy/' + stock._id}>BUY</a></Reactable.Td>
                </Reactable.Tr>
            );
        });
        return (
            <Reactable.Table className="table"  sortable={true} filterable={['Name', 'Trade Date']}>
                {stockNodes}
            </Reactable.Table>
        );
    }
});

var Stocks = React.createClass({
    render: function() {
        var stockNodes = this.props.data.map(function (stock) {
            var stockTradeDate = $.datepicker.formatDate("dd-M-yy", new Date(stock.tradeDate));
            return (
                <Reactable.Tr>
                    <Reactable.Td column="Trade Date" data={stockTradeDate} />
                    <Reactable.Td column="Name" data={stock.name}/>
                    <Reactable.Td column="Quantity" data={stock.quantity}/>
                    <Reactable.Td column="Base Price" data={stock.currency.name + ' ' + stock.price}/>
                    <Reactable.Td column="Profit/Loss ($)" data={stock.profitLoss}/>
                    <Reactable.Td column="Sell Qty"><input type="number" id={stock.userStockId} /></Reactable.Td>
                    <Reactable.Td column=""><a href={'/sell/stock/' + stock.userStockId + '/sell/' +  $("#datetimepicker1").val() + '/quantity/' + (($("#" + stock.userStockId).val() == "") ? 0 : $("#" + stock.userStockId).val()) }>SELL</a></Reactable.Td>
                </Reactable.Tr>
            );
        });
        if (stockNodes.length == 0) {
            return (
                <h4>Nothing to sell yet.</h4>
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

React.render(<DateStockList url="/view/dashboard" />, document.getElementById('currency'));
React.render(<StockStatus url="/view/dashboard" />, document.getElementById('stockStatus'));
React.render(<UserStockList url="/view/dashboard" />, document.getElementById('sellStocks'));
