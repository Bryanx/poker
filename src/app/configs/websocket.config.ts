import { InjectableRxStompConfig } from '@stomp/ng2-stompjs';

export const websocketConfig: InjectableRxStompConfig = {
  brokerURL: 'wss://poker-game-service.herokuapp.com/connect/websocket',
  // brokerURL: 'ws://localhost:5001/connect/websocket',

  // Headers
  // Typical keys: login, passcode, host
  connectHeaders: {
    login: 'guest',
    passcode: 'guest'
  },

  // How often to heartbeat?
  // Interval in milliseconds, set to 0 to disable
  heartbeatIncoming: 0, // Typical value 0 - disabled
  heartbeatOutgoing: 20000, // Typical value 20000 - every 20 seconds

  // Wait in milliseconds before attempting auto reconnect
  // Set to 0 to disable
  // Typical value 500 (500 milli seconds)
  reconnectDelay: 200,


};
