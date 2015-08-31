
var StockForm = React.createClass({
    handleSubmit: function (e) {
        console.log("StockForm.handleSubmit()");
        e.preventDefault();

        var formData = $("#stockForm").serialize();

        var saveUrl = "/stock/buy";
        $.ajax({
            url: saveUrl,
            method: 'POST',
            dataType: 'json',
            data: formData,
            cache: false,
            success: function (data) {
                console.log(data)
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(saveUrl, status, err.toString());
            }.bind(this)
        });

        // clears the form fields
        React.findDOMNode(this.refs.id).value = '';
        React.findDOMNode(this.refs.name).value = '';
        React.findDOMNode(this.refs.price).value = '';
        React.findDOMNode(this.refs.orderedDate).value = '';
        return;
    },
    componentDidMount: function() {
        $(function () {
            $('#datetimepicker1').datepicker();
        });
    },
    render: function () {
        return (
            <form id="stockForm" onSubmit={this.handleSubmit}>
                <div className="row">
                    <div class="page-header"><h1>Buy Stock</h1></div>
                </div>
                <div className="row">
                    <div className="col-xs-6">
                        <div className="form-group">
                            <input type="hidden" name="id" ref="id"/>
                            <input type="hidden" name="userId" ref="userId" value="testId" />
                            <input type="text" name="stock.name" required="required" ref="name" placeholder="Name"
                                   className="form-control"/>
                            <input type="number" name="price" ref="price" required="required" placeholder="Buy Price"
                                   className="form-control"/>

                            <input id='datetimepicker1' type='text' name="orderedDate" ref="orderedDate" required="required" className="form-control"/>
                        </div>
                    </div>
                </div>

                <div className="row">
                    <div className="col-xs-3">
                    </div>
                    <div className="col-xs-3">
                        <input type="submit" className="btn btn-block btn-info" value="Buy"/>
                    </div>
                </div>
            </form>
        );
    }
});

var StockList = React.createClass({
    loadStocks: function() {
        console.log("StockList.loadStocks()");
        $.ajax({
            url: "/stock/user/testId",
            dataType: 'json',
            cache: false,
            success: function(data) {
                console.log("StockList.dataSet size:" + data.length);
                this.setState({data: data});
            }.bind(this),
            error: function(xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    getInitialState: function() {
        console.log("StockList.getInitialState()");
        return {data: []};
    },
    componentDidMount: function() {
        console.log("StockList.componentDidMount()");
        this.loadStocks();
        //setInterval(this.loadStocks, this.props.pollInterval);
    },
    render: function() {
        return (
            <div className="">
                <h1>Sell Stocks</h1>
                <Stocks data={this.state.data} />

                <div className="row">
                    <div className="col-xs-3">
                        <input type="number" name="price" ref="price" required="required" placeholder="Sell Price"
                               className="form-control"/>
                    </div>
                    <div className="col-xs-3">
                        <input type="submit" className="btn btn-block btn-info" value="Sell"/>
                    </div>
                </div>
            </div>
        );
    }
});

var Stocks = React.createClass({
    render: function() {
        var stockNodes = this.props.data.map(function (stock) {
            return (
                <Stock key={stock.id} name={stock.name} />
            );
        });

        return (
            <div className="well">
                {stockNodes}
            </div>
        );
    }
});

var Stock = React.createClass({
    render: function() {
        return (
            <blockquote>
                <span>{this.props.name}</span><input type="radio" name="sell" class="custom-radio"></input>
            </blockquote>
        );
    }
});

React.render(<StockForm url="/stock" />, document.getElementById('buyStocks'));
React.render(<StockList url="/stock" />, document.getElementById('showUserStocks'));
