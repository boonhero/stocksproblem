var MessageList = React.createClass({
    loadMessages: function() {
        console.log("MessageList.loadMessages()");
        $.ajax({
            url: "/messages",
            dataType: 'json',
            cache: false,
            success: function(data) {
                console.log("MessageList.dataSet size:" + data.length);
                this.setState({data: data});
            }.bind(this),
            error: function(xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    getInitialState: function() {
        console.log("MessageList.getInitialState()");
        return {data: []};
    },
    componentDidMount: function() {
        console.log("MessageList.componentDidMount()");
        this.loadMessages();
        setInterval(this.loadMessages, this.props.pollInterval);
    },
    render: function() {
        return (
            <div className="">
                <h1>Messages</h1>
                <Messages data={this.state.data} />
            </div>
        );
    }
});

var Messages = React.createClass({
    render: function() {
        var messageNodes = this.props.data.map(function (message) {
            return (
                <Message key={message.id} message={message.message} />
            );
        });

        return (
            <div className="well">
                {messageNodes}
            </div>
        );
    }
});

var Message = React.createClass({
    render: function() {
        return (
            <blockquote>
                <p>{this.props.message}</p>
            </blockquote>
        );
    }
});

var MessageForm = React.createClass({
    handleSubmit: function (e) {
        console.log("MessageForm.handleSubmit()");
        e.preventDefault();

        var formData = $("#messageBoxForm").serialize();

        var saveUrl = "/messages";
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
        React.findDOMNode(this.refs.message).value = '';
        return;
    },
    render: function () {
        return (
            <form id="messageBoxForm" onSubmit={this.handleSubmit}>
                <div className="row">
                    <div class="page-header"><h1>Create message</h1></div>
                </div>
                <div className="row">
                    <div className="col-xs-6">
                        <div className="form-group">
                            <input type="hidden" name="id" ref="id"/>
                            <input type="text" name="message" required="required" ref="message" placeholder="Message"
                                   className="form-control"/>
                        </div>
                    </div>
                </div>

                <div className="row">
                    <div className="col-xs-3">
                    </div>
                    <div className="col-xs-3">
                        <input type="submit" className="btn btn-block btn-info" value="Create"/>
                    </div>
                </div>
            </form>
        );
    }
});

React.render(<MessageForm url="/box" />, document.getElementById('createMessage'));
React.render(<MessageList url="/box" pollInterval={2000} />, document.getElementById('messageList'));