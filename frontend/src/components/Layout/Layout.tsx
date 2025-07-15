import { Outlet, Link } from "react-router-dom";
import styles from "./Layout.module.css";

const Layout = () => {
  return (
    <div className={styles.container}>
      <nav className={styles.nav}>
        <Link to="/" className={styles.link}>
          Book List
        </Link>
        <Link to="/add" className={styles.link}>
          Add Book
        </Link>
      </nav>
      <main className={styles.main}>
        <Outlet />
      </main>
    </div>
  );
};

export default Layout;