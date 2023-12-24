import { Client } from '@stomp/stompjs';

const stompClient = new Client({
    brokerURL: 'ws://localhost:8080/ws',

    onWebSocketClose : (error) => {
        console.error('Error with websocket', error)
    },

    onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    },
})

export default stompClient;