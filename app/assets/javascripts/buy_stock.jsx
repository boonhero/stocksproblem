var BuyStock = React.createClass({
    handleOnChange: function(e) {
        $("#total").text(React.findDOMNode(this.refs.quantity).value * $("#price").text())
    },
    componentDidMount: function() {
        React.findDOMNode(this.refs.stockId).value = $("#stockId").val();
    },
    render: function () {
        return (
            <form id="stockForm" action="/stock/buy" method="POST">
                <div className="row">
                    <div class="page-header"><h1>Buy Stock</h1></div>
                    <LoadStock></LoadStock>
                </div>
                <div className="row">
                    <div className="col-xs-3">
                        <div className="form-group">
                            <input type="hidden" name="userId" ref="userId" value="testId" />
                            <input type="hidden" name="stockId" ref="stockId" value="" />
                            <input type="number" name="quantity" ref="quantity" required="required" placeholder="Quantity"
                                   className="form-control" onChange={this.handleOnChange}/>
                            <label>Total: <span id="totalCurrency"></span> <span id="total">0</span></label>
                        </div>
                    </div>
                </div>

                <div className="row">
                    <div className="col-xs-3">
                        <input type="submit" className="btn btn-block btn-info" value="Buy Stock"/>
                    </div>
                </div>
            </form>
        );
    }
});

var LoadStock = React.createClass({
    loadStock: function() {
        $.ajax({
            url: "/stock/" + $("#stockId").val(),
            dataType: 'json',
            cache: false,
            success: function(data) {
                $("#totalCurrency").text(data.currency.name)
                this.setState({data: [data]});
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
        this.loadStock();
    },
    render: function () {
        var stock = this.state.data;
        return (
            <Stock data={this.state.data}></Stock>
        )
    }
});

var Stock = React.createClass({
    render: function() {
        var stockNodes = this.props.data.map(function (stock) {
            var stockTradeDate = $.datepicker.formatDate("dd-M-yy", new Date(stock.tradeDate));
            return (
                <div>
                    <label>Name: <span>{stock.name}</span></label><br/>
                    <label>Price: <span>{stock.currency.name} </span><span id="price">{stock.price}</span></label><br/>
                    <label>Trade Date: <span>{stockTradeDate}</span></label><br/>
                </div>
            );
        });

        return (
            <div className="stock">
                {stockNodes}
            </div>
        );
    }
});

React.render(<BuyStock url="/view/stock/buy" />, document.getElementById('buyStock'));