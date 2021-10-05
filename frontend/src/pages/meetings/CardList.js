import Card from "./Card";

const CardList = () => {
  return (
    <div className="grid grid-cols-3 gap-6">
      <Card />
      <Card />
      <Card />
      <Card />
      <Card />
    </div>
  )
}

export default CardList;