/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const SectionTitle = ({text, marginTop}) => {
  const sectionTitleStyle = css`
    color: white;
    font-weight: 700;
    font-size: 36px;
    font-style: italic;
    margin-top: ${marginTop};
    white-space: nowrap;
  `

  return (
    <span css={sectionTitleStyle}>{text}</span>
  )
}

export default SectionTitle;

