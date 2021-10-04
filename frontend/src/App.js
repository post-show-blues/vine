import { Route } from 'react-router-dom';
import {Index as HomeIndex} from "./pages/home/Index"
import {Index as MeetingsNew} from "./pages/meetings/new/Index";
import {Index as MeetingsJoin} from "./pages/meetings/join/Index";
import {Index as EditIndex} from "./pages/mypage/Index";

function App() {
  return (
    <div>
      <Route exact path="/" component={HomeIndex} />
      <Route exact path="/meetings/new" component={MeetingsNew} />
      <Route exact path="/meetings/join" component={MeetingsJoin} />
      <Route exact path="/meetings" component={HomeIndex} /> {/*  */}
      <Route exact path="/search" component={HomeIndex} /> {/* 검색(4개페이지) */}
      <Route exact path="/about" component={HomeIndex} /> {/* 소개 */}
      <Route exact path="/mypage" component={EditIndex} /> {/* 마이페이지 */}
      <Route exact path="/mypage/edit" component={HomeIndex} /> {/* 마이페이지 수정 */}
      <Route exact path="/signup" component={HomeIndex} /> {/* 로그인 */}
      <Route exact path="/signin" component={HomeIndex} /> {/* 회원가입 */}
      <Route exact path="/participation" component={HomeIndex} /> {/* 회원가입 */}
      {/* 여기에 라우팅 주소 추가해서 만드시면 됩니다 */}
    </div>
  );
}

export default App;
