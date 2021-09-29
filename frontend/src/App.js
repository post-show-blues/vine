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
      <Route path="/meetings" component={HomeIndex} /> {/*  */}
      <Route path="/search" component={HomeIndex} /> {/* 검색(4개페이지) */}
      <Route path="/about" component={HomeIndex} /> {/* 소개 */}
      <Route path="/mypage" component={HomeIndex} /> {/* 마이페이지 */}
      <Route path="/mypage/edit" component={HomeIndex} /> {/* 마이페이지 수정 */}
      <Route path="/signup" component={HomeIndex} /> {/* 로그인 */}
      <Route path="/signin" component={HomeIndex} /> {/* 회원가입 */}
      <Route path="/participation" component={HomeIndex} /> {/* 회원가입 */}
      {/* 여기에 라우팅 주소 추가해서 만드시면 됩니다 */}
    </div>
  );
}

export default App;
