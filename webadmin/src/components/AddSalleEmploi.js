import React, { useState, useEffect } from "react";
import { Formik, Form, Field } from "formik";
import { TextField, Button, Box, Typography, MenuItem, Select, InputLabel, FormControl } from "@mui/material";
import * as Yup from "yup";
import { db } from "../firebase"; // Your Firebase config
import { doc, setDoc } from "firebase/firestore";

const AddSalleEmploi = () => {
  const [salles, setSalles] = useState([]);

  useEffect(() => {
    setSalles([
      { name: "Salle 1", id: "1" },
      { name: "Salle 2", id: "2" },
      { name: "Salle 3", id: "3" },
    ]);
  }, []);

  const validationSchema = Yup.object({
    salle: Yup.string().required("Salle selection is required"),
    excelFile: Yup.mixed().required("Excel file is required").test(
      "fileSize",
      "File too large",
      (value) => !value || value.size <= 5 * 1024 * 1024
    ),
  });

  const handleSubmit = async (values, { resetForm }) => {
    try {
      const base64 = await convertFileToBase64(values.excelFile);

      const salleData = {
        salle: values.salle,
        excelFileBase64: base64,
      };

      await setDoc(doc(db, "emploi", values.salle), salleData);

      alert("Salle emploi added successfully!");
      resetForm();
    } catch (error) {
      console.error("Error uploading file:", error);
      alert("Failed to upload Excel file");
    }
  };

  const convertFileToBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onloadend = () => resolve(reader.result.split(",")[1]);
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  };

  return (
    <Box my={4} p={3} border={1} borderRadius={2} borderColor="grey.300">
      <Typography variant="h6" gutterBottom>
        Add Salle Emploi
      </Typography>
      <Formik
        initialValues={{
          salle: "",
          excelFile: null,
        }}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
      >
        {({ setFieldValue, errors, touched }) => (
          <Form>
            <Box mb={2}>
              <FormControl fullWidth>
                <InputLabel id="salle-label">Salle</InputLabel>
                <Field as={Select} name="salle" labelId="salle-label" label="Salle" fullWidth>
                  {salles.map((salle) => (
                    <MenuItem key={salle.id} value={salle.name}>
                      {salle.name}
                    </MenuItem>
                  ))}
                </Field>
              </FormControl>
              {errors.salle && touched.salle && <div>{errors.salle}</div>}
            </Box>
            <Box mb={2}>
              <input
                type="file"
                accept=".xlsx,.xls"
                onChange={(e) => setFieldValue("excelFile", e.target.files[0])}
              />
              {errors.excelFile && touched.excelFile && <div>{errors.excelFile}</div>}
            </Box>
            <Button type="submit" variant="contained" color="primary">
              Add Salle Emploi
            </Button>
          </Form>
        )}
      </Formik>
    </Box>
  );
};

export default AddSalleEmploi;
