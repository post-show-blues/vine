import { Route } from 'react-router-dom';
import {Index as HomeIndex} from "./pages/home/Index"
import {Index as MeetingsNew} from "./pages/meetings/new/Index";
import {Index as MeetingsJoin} from "./pages/meetings/join/Index";

function App() {
  return (
    <div>
      <Route exact path="/" component={HomeIndex} />
      <Route path="/meetings/new" component={MeetingsNew} />
      <Route path="/meetings/join" component={MeetingsJoin} />
      {/* 여기에 라우팅 주소 추가해서 만드시면 됩니다 */}
    </div>
  );
}

export default App;
