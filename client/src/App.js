import React from 'react';
import { 
  Button, 
  Box 
} from 'rebass';
import { 
  Label, 
  Input, 
  Textarea, 
  Switch, 
  Select 
} from '@rebass/forms'

import { ThemeProvider } from 'emotion-theming'
import theme from '@rebass/preset'

import './App.css';

const App = () => {

  const [connection, setConnection] = React.useState(null)
  const [response, setResponse] = React.useState(null);
  const [config, setConfig] = React.useState({
    port: '',
    message: ''
  });

  const getParameterByName = (name, url) => {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

  const initialize = () => {
    var connection = new WebSocket(`ws://127.0.0.1:${getParameterByName("port", window.location.href) || 9000}`);
    
    connection.onopen = () => {
      console.log('Connected');
      connection.send(JSON.stringify({
        acknowledgment: true
      }));
    };

    connection.onerror = () => {
      console.log('Connection could not established');
    };

    connection.onmessage = (event) => {

      try {
        const obj = JSON.parse(event.data);
        setConfig({
          port: parseInt(obj.port),
          message : obj.message
        })
        setResponse("Connection established!");
      } catch(e) {
        setResponse(event.data);
      }
    };

    connection.onclose = () => {
      console.log('Closing')
      setConnection(null);
      setResponse(null);
      setConfig({
        message: '',
        port: ''
      })
    }

    setConnection(connection)
  }

  const close = () => {
    connection.close()
    setConnection(null);
  }

  React.useEffect(initialize, [])

  const handleConfigSubmit = (e) => {
    e.persist();
    e.preventDefault();

    connection &&
      connection.send(JSON.stringify({
        message : config.message,
        port: parseInt(config.port)
      }))

    window.location.href=`?port=${config.port}&message=${config.message}`

  }

  const handleOperationSubmit = (e) => {
    e.persist();
    e.preventDefault();

    connection &&
      connection.send(JSON.stringify({
        number1 : e.target.number1.value,
        operation : e.target.operation.value,
        number2 : e.target.number2.value
      }))
      
  }

  const handleChange = (e) => {
    e.target.value && setConfig({
      ...config,
      [e.target.name] : e.target.name === "port" ? 
        parseInt(e.target.value) :
        e.target.value
    })
  }

  const handleConnect = () => {
    connection ?
      close() :
      initialize()
  }

  return (
    <ThemeProvider theme={theme}>

      <div className="header">
        <h1>Simple Calculator</h1>

        <div className="connect-button">
          <span>Connect</span>
          <Switch
            checked={connection}
            onClick={handleConnect}
          />
        </div>
      </div>

      <div className="content">
        {!connection && <div className="disabled"></div>}
        <form id="options" onSubmit={handleConfigSubmit}>
          <h4>
            Options
          </h4>
          <Box className="mb">
            <Label htmlFor='port'>Port</Label>
            <Input
              value={config.port}
              onChange={handleChange}
              id='port'
              name='port'
              type='number'
              variant='secondary'
            />
          </Box>
          <Box className="mb">
            <Label htmlFor='message'>Message</Label>
            <Textarea
              variant='secondary'
              id='message'
              name='message'
              value={config.message}
              onChange={handleChange}
            />
          </Box>

          <Button
            type="submit"
            variant='secondary'
            className="mb"
          >
            Change Settings
          </Button>
        </form>

        <form id="equation" className="mb" onSubmit={handleOperationSubmit}>
          <h4>
            Calculation
          </h4>
          <div className="form">
            <Input
              className="input"
              name="number1"
              type='number'
              variant='secondary'
            />

            <Select
              name="operation"
            >
              <option value="*">*</option>
              <option value="+">+</option>
              <option value="-">-</option>
              <option value="/">/</option>
            </Select>
            <Input
              className="input"
              name="number2"
              type='number'
              variant='secondary'
            />
            <Button
              type="submit"
              variant='secondary'
            >
              Submit
          </Button>
          </div>
        </form>

        <div>
          {response}
        </div>

      </div>


    </ThemeProvider>
  );
}

export default App;
