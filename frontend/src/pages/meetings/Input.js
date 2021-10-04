/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { innerShadow, dropShadow } from "../constant";

const Input = (props) => {
  const inputStyle = css`
    width: 100%;
    height: ${props.height}px;
    background-color: #333333;
    border-radius: 25px;
    height: ${props.height}px;
    font-size: 1.5rem;
    font-weight: 700;
    box-shadow: ${innerShadow};
    
    input {
      position: relative;
      color: #d6d6d6;
      background-color: #333333;
      border-radius: 1.5rem;
      padding: 0 2rem 0 calc(1rem + 168px);
      width: 100%;
      box-shadow: ${innerShadow};
      ::-webkit-calendar-picker-indicator {
        filter: invert(1);
      }
    }

    &[data-descr]::after {
        border-radius: 25px;
        background-color: #575757;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        box-shadow: ${dropShadow};
        color: #090909;
        min-width: 168px;
        width: 168px;
        font-weight: 700;
        position: absolute;
        content: attr(data-descr);
        z-index: 2;
        height: ${props.height}px;
      }
  `
  return (
    <div css={inputStyle} className="flex" data-descr={props.name}>
      <input type={props.type} />
    </div>
  )
}

export default Input


