import { Route } from 'react-router-dom';
import MeetingsNew from "./MeetingsNew";
import Home from './Home';
function App() {
  return (
    <div>
      <Route exact path="/" component={Home} />
      <Route path="/meetings/new" component={MeetingsNew} />
      {/* 여기에 라우팅 주소 추가해서 만드시면 됩니다 */}
    </div>
  );
}

export default App;
