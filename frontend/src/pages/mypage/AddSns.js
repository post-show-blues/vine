/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'



const AddSns = ({text, icon}) => {

  const style = css`
    background-color: #7F7F7F;
    border-radius: 20px;
    display: flex;
    padding: 0.25rem 0.75rem;
    align-items: center;
    margin-right: 1rem;
    font-weight: 700;
    
  `

  return(
    <div css={style}>
      <FontAwesomeIcon icon={icon}/>
      <span className="ml-2 text-lg">ADD {text}</span>
    </div>
  )
}

export default AddSns;