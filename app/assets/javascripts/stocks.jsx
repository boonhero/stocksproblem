
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
                            <input type="number" name="price" ref="price" required="required" placeholder="Price"
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

React.render(<StockForm url="/stock" />, document.getElementById('buyStocks'));
