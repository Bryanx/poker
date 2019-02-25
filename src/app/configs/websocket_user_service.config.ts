import { InjectableRxStompConfig } from '@stomp/ng2-stompjs';

export const websocketConfigUserService: InjectableRxStompConfig = {
  brokerURL: 'ws://localhost:5000/connect/websocket',
  // brokerURL: 'wss://poker-user-service.herokuapp.com/connect/websocket',

  // Headers
  // Typical keys: login, passcode, host
  connectHeaders: {
    login: 'guest1',
    passcode: 'guest1'
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
