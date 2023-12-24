import {useEffect, useState, useRef} from "react";
import "./App.css";
import stompClient from "./server";
import InputField from "./components/InputField/InputField";
import MessageContainer from "./components/MessageContainer/MessageContainer";

function App() {
    const [user, setUser] = useState(null);
    const [message, setMessage] = useState('');
    const [messageList, setMessageList] = useState([]);
    const [isConnected, setIsConnected] = useState(false);
    const prevIsConnected = useRef(false);

    console.log("messageList : ", messageList);

    useEffect(() => {
        stompClient.activate();

        stompClient.onConnect = (frame) => {
            setIsConnected(true);
            console.log('Connected: ' + frame);
        };

        stompClient.onDisconnect = (frame) => {
            setIsConnected(false);
            console.log('DisConnected: ' + frame);
        }

        stompClient.onStompError = (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
            setIsConnected(false);
        };

        stompClient.onWebSocketError = (error) => {
            console.error('Error with websocket', error);
            setIsConnected(false);
        };

    }, []);

    useEffect(() => {
        if (prevIsConnected.current === isConnected) return;
        if (isConnected) {
            const userName = prompt("당신의 이름을 입력하세요");
            stompClient.subscribe('/sub/room/1', (res) => {
                const receivedMessage = JSON.parse(res.body);
                setMessageList((prev) => [...prev, receivedMessage]);
            });
            setUser(userName);
            stompClient.publish({
                destination: "/pub/room/1",
                body: JSON.stringify({'type': 'CONNECT', 'user': userName})
            });
        } else {
            stompClient.publish({
                destination: "/pub/room/1",
                body: JSON.stringify({'type': 'DISCONNECT', 'user': user})
            });
            setUser(null);
        }
        prevIsConnected.current = isConnected;
    }, [isConnected, user]);


    const sendMessage = (event) => {
        event.preventDefault();
        if (stompClient.connected) {
            stompClient.publish({
                destination: "/pub/room/1",
                body: JSON.stringify({'type': 'MESSAGE', 'user': user, "message": message})
            });
        } else {
            console.error('STOMP client is not connected.');
        }
    };

    return (<div>
        <div className="App">
            <MessageContainer messageList={messageList} user={user}/>
            <InputField message={message} setMessage={setMessage} sendMessage={sendMessage}/>
        </div>
    </div>);
}

export default App;
